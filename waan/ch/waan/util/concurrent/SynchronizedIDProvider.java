package ch.waan.util.concurrent;

import ch.waan.util.id.IDProvider;

/**
 * This IDProvider Implementation provides a synchronisation layer for the
 * IDProvider system. Using a provider that is wrapped into an instance of this
 * class can enforce completely synchronised operation. Any access to the
 * provider through this class will always be synchronised.
 *
 * @author Andreas Wälchli
 * @version 1.1, 2014-11-24s
 */
public class SynchronizedIDProvider implements IDProvider {

	private IDProvider	backer;

	/**
	 * Instantiate a new synchronised IDProvider based on the provided provider.
	 *
	 * @param backer
	 *            the provider this instance should be backed by. This may not
	 *            be {@code null}
	 * @throws NullPointerException
	 *             if the backer is {@code null}
	 */
	public SynchronizedIDProvider(IDProvider backer) {
		if (backer == null)
			throw new NullPointerException("backer may not be null");
		this.backer = backer;
	}

	@Override
	public synchronized boolean isFull() {
		return this.backer.isFull();
	}

	@Override
	public synchronized boolean isUsed(int id) {
		return this.backer.isUsed(id);
	}

	@Override
	public synchronized boolean set(int id) {
		return this.backer.set(id);
	}

	@Override
	public synchronized boolean free(int id) {
		return this.backer.free(id);
	}

	@Override
	public synchronized int get() {
		return this.backer.get();
	}

	@Override
	public synchronized boolean isEmpty() {
		return this.backer.isEmpty();
	}

	@Override
	public synchronized long getFreeIDCount() {
		return this.backer.getFreeIDCount();
	}

}